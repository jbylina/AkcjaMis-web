(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('ChristmasPackageChange', ChristmasPackageChange);

    ChristmasPackageChange.$inject = ['$resource', 'DateUtils'];

    function ChristmasPackageChange ($resource, DateUtils) {
        var resourceUrl =  'api/christmas-package-changes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.time = DateUtils.convertLocalDateFromServer(data.time);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.time = DateUtils.convertLocalDateToServer(data.time);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.time = DateUtils.convertLocalDateToServer(data.time);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
