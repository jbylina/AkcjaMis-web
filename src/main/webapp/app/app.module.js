(function() {
    'use strict';

    angular
        .module('akcjamisApp', [
            'ngStorage',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'ds.objectDiff',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'ngAnimate',
            'xeditable'
        ])
        .run(run);

    run.$inject = ['stateHandler','editableOptions'];

    function run(stateHandler,editableOptions) {
        stateHandler.initialize();
        editableOptions.theme = 'bs3';
    }
})();
