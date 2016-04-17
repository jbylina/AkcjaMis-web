(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('ChristmasPackageSearch', ChristmasPackageSearch);

    ChristmasPackageSearch.$inject = ['$resource'];

    function ChristmasPackageSearch($resource) {
        var resourceUrl =  'api/_search/christmas-packages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
