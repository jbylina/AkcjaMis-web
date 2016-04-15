(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('PackageNoteSearch', PackageNoteSearch);

    PackageNoteSearch.$inject = ['$resource'];

    function PackageNoteSearch($resource) {
        var resourceUrl =  'api/_search/package-notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
