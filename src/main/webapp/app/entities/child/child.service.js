(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('Child', Child);

    Child.$inject = ['$resource', 'DateUtils'];

    function Child ($resource, DateUtils) {
        var resourceUrl =  'api/children/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthYear = DateUtils.convertLocalDateFromServer(data.birthYear);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birthYear = DateUtils.convertLocalDateToServer(data.birthYear);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthYear = DateUtils.convertLocalDateToServer(data.birthYear);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
