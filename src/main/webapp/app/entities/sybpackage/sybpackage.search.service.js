(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('SybpackageSearch', SybpackageSearch);

    SybpackageSearch.$inject = ['$resource'];

    function SybpackageSearch($resource) {
        var resourceUrl =  'api/_search/sybpackages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
