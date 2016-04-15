(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('FamilySearch', FamilySearch);

    FamilySearch.$inject = ['$resource'];

    function FamilySearch($resource) {
        var resourceUrl =  'api/_search/families/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
