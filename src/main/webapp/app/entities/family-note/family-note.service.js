(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('FamilyNote', FamilyNote);

    FamilyNote.$inject = ['$resource', 'DateUtils'];

    function FamilyNote ($resource, DateUtils) {
        var resourceUrl =  '/api/families/:familyId/family-notes/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                url: '/api/families/:id/family-notes',
                isArray: true
            },
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
                url: '/api/families/:id/family-notes',
                transformRequest: function (data) {
                    if (data.tags == null)
                        data.tags = Array();
                    data.time = DateUtils.convertLocalDateToServer(data.time);
                    return angular.toJson(data);

                }
            },
            'save': {
                method: 'POST',
                url: '/api/families/:id/family-notes',
                transformRequest: function (data) {
                    if (data.tags == null)
                        data.tags = Array();
                    data.time = DateUtils.convertLocalDateToServer(data.time);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
