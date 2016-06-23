(function() {
    'use strict';
    angular
        .module('akcjamisApp')
        .factory('ChristmasPackageNote', ChristmasPackageNote);

    ChristmasPackageNote.$inject = ['$resource'];

    function ChristmasPackageNote ($resource) {
        var resourceUrl =  'api/christmas-packages/:id/notes';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT',
                transformRequest: function (data) {
                    data.lastModifiedDate = data.createdDate;
                    data.lastModifiedBy = 'admin';
                    console.log(data);
                    return angular.toJson(data);
                }
             },
            'delete': { method:'DELETE',
                url: '/api/christmas-packages/:packageId/notes/:id'
            }
        });
    }
})();
