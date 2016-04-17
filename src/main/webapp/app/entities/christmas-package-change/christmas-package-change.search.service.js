(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('ChristmasPackageChangeSearch', ChristmasPackageChangeSearch);

    ChristmasPackageChangeSearch.$inject = ['$resource'];

    function ChristmasPackageChangeSearch($resource) {
        var resourceUrl =  'api/_search/christmas-package-changes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
