(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .factory('ChildSearch', ChildSearch);

    ChildSearch.$inject = ['$resource'];

    function ChildSearch($resource) {
        var resourceUrl =  'api/_search/children/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
