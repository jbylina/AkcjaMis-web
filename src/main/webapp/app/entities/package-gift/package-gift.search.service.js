(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('PackageGiftSearch', PackageGiftSearch);

    PackageGiftSearch.$inject = ['$resource'];

    function PackageGiftSearch($resource) {
        var resourceUrl =  'api/_search/package-gifts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
