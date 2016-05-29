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
                'api/clustered',
                {},
                {
                    'query':
                    {
                        method: 'POST',
                        isArray: true
                    }
                }
            );
        }
    }
)
();
