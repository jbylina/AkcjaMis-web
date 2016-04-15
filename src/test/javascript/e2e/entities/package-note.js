'use strict';

describe('PackageNote e2e test', function () {

    var username = element(by.id('username'));
    var password = element(by.id('password'));
    var entityMenu = element(by.id('entity-menu'));
    var accountMenu = element(by.id('account-menu'));
    var login = element(by.id('login'));
    var logout = element(by.id('logout'));

    beforeAll(function () {
        browser.get('/');
        browser.driver.wait(protractor.until.elementIsVisible(element(by.css('h1'))));

        accountMenu.click();
        login.click();

        username.sendKeys('admin');
        password.sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    it('should load PackageNotes', function () {
        entityMenu.click();
        element(by.css('[ui-sref="package-note"]')).click().then(function() {
            expect(element.all(by.css('h2')).first().getText()).toMatch(/PackageNotes/);
        });
    });

    it('should load create PackageNote dialog', function () {
        element(by.css('[ui-sref="package-note.new"]')).click().then(function() {
            expect(element(by.css('h4.modal-title')).getText()).toMatch(/Create or edit a PackageNote/);
            element(by.css('button.close')).click();
        });
    });

    afterAll(function () {
        accountMenu.click();
        logout.click();
    });
});
