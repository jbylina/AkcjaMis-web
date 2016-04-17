(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('SubpackageSearch', SubpackageSearch);

    SubpackageSearch.$inject = ['$resource'];

    function SubpackageSearch($resource) {
        var resourceUrl =  'api/_search/subpackages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
