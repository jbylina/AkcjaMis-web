(
    function()
    {
        'use strict';

        angular
            .module('akcjamisApp')
            .factory('ChristmasPackagesMap', ChristmasPackagesMap);

        ChristmasPackagesMap.$inject = ['$resource'];

        function ChristmasPackagesMap($resource)
        {
            return $resource
            (
                'api/clustered/1',
                {},
                {
                    'query':
                    {
                        method: 'GET',
                        isArray: true
                    }
                }
            );
        }
    }
)
();
