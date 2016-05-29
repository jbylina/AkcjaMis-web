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
                {
                    distance: 0.01
                },
                {
                    'query':
                    {
                        method: 'POST',
                        //isArray: true
                    }
                }
            );
        }
    }
)
();
