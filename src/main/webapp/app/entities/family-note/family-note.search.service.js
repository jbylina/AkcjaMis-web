(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('FamilyNoteSearch', FamilyNoteSearch);

    FamilyNoteSearch.$inject = ['$resource'];

    function FamilyNoteSearch($resource) {
        var resourceUrl =  'api/_search/family-notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
