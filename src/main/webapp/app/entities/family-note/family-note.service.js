(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('FamilyNote', FamilyNote);

    FamilyNote.$inject = ['$resource', 'DateUtils'];

    function FamilyNote ($resource, DateUtils) {
        var resourceUrl =  'api/family-notes/:id';

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
