(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('ChristmasPackageNoteSearch', ChristmasPackageNoteSearch);

    ChristmasPackageNoteSearch.$inject = ['$resource'];

    function ChristmasPackageNoteSearch($resource) {
        var resourceUrl =  'api/_search/christmas-package-notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
