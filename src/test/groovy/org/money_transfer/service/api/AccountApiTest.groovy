package org.money_transfer.service.api

import org.glassfish.jersey.uri.internal.JerseyUriBuilder
import org.money_transfer.service.exception.ApiRequestValidationException
import org.money_transfer.service.exception.DuplicatedResourceException
import org.money_transfer.service.exception.ResourceNotFoundException
import org.money_transfer.service.model.Account
import org.money_transfer.service.repository.AccountRepository
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validator
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.UriInfo

import static javax.ws.rs.core.Response.Status.CREATED

/**
 * @author Maksym Bondarenko
 * @version 1.0 16.03.19
 */

class AccountApiTest extends Specification {

    def validator = Stub(Validator.class)
    def accountRepository = Stub(AccountRepository.class)

    def "should successfully create account"() {
        given:
        def accountNumber = 12345
        def accountApi = new AccountApi(accountRepository, validator)
        def account = new Account('acc1', accountNumber, 123.54G)
        def uriInfo = Stub(UriInfo.class)
        def host = 'localhost'
        def path = '/api/account'
        def scheme = 'https'

        when:
        def response = accountApi.creatAccount(account, uriInfo)

        then:
        uriInfo.absolutePathBuilder >> new JerseyUriBuilder().scheme(scheme).host(host).path(path)
        accountRepository.isPresent(accountNumber) >> false

        and:
        response.status == CREATED.statusCode
        response.getHeaderString(HttpHeaders.LOCATION) == scheme + '://' + host + path + '/' + accountNumber
    }

    def "should successfully return account"() {
        given:
        def accountNumber = 12345
        def accountApi = new AccountApi(accountRepository, validator)
        def expectedAccount = new Account('acc1', accountNumber, 123.54G)

        when:
        def actualAccount = accountApi.getAccount(accountNumber)

        then:
        accountRepository.getAccount(accountNumber) >> expectedAccount

        and:
        actualAccount == expectedAccount
    }

    def "should throw ResourceNotFoundException on not found account"() {
        given:
        def accountNumber = 12345
        def accountApi = new AccountApi(accountRepository, validator)

        when:
        accountApi.getAccount(accountNumber)

        then:
        accountRepository.getAccount(accountNumber) >> null

        and:
        thrown ResourceNotFoundException
    }

    def "should throw DuplicatedResourceException on creating duplicated account"() {
        given:
        def accountNumber = 12345
        def accountApi = new AccountApi(accountRepository, validator)
        def account = new Account('acc1', accountNumber, 123.54G)
        def uriInfo = Stub(UriInfo.class)
        def host = 'localhost'
        def path = '/api/account'
        def scheme = 'https'

        when:
        accountApi.creatAccount(account, uriInfo)

        then:
        uriInfo.absolutePathBuilder >> new JerseyUriBuilder().scheme(scheme).host(host).path(path)
        accountRepository.isPresent(accountNumber) >> true

        and:
        thrown DuplicatedResourceException
    }

    def "should throw ApiRequestValidationException on invalid account"() {
        given:
        def accountNumber = 12345
        def accountApi = new AccountApi(accountRepository, validator)
        def account = new Account('acc1', accountNumber, 123.54G)
        def uriInfo = Stub(UriInfo.class)
        def host = 'localhost'
        def path = '/api/account'
        def scheme = 'https'

        when:
        accountApi.creatAccount(account, uriInfo)

        then:
        uriInfo.absolutePathBuilder >> new JerseyUriBuilder().scheme(scheme).host(host).path(path)
        validator.validate(account) >> [Stub(ConstraintViolation.class)]

        and:
        thrown ApiRequestValidationException
    }
}
