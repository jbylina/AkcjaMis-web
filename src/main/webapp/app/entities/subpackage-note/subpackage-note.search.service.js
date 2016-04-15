(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('SubpackageNoteSearch', SubpackageNoteSearch);

    SubpackageNoteSearch.$inject = ['$resource'];

    function SubpackageNoteSearch($resource) {
        var resourceUrl =  'api/_search/subpackage-notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
