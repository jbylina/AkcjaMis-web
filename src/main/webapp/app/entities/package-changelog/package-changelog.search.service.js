(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('PackageChangelogSearch', PackageChangelogSearch);

    PackageChangelogSearch.$inject = ['$resource'];

    function PackageChangelogSearch($resource) {
        var resourceUrl =  'api/_search/package-changelogs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
