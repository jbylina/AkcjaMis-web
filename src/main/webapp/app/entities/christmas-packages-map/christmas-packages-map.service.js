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
                'api/families/cluster?distance=:distance', {},
                {
                    'query':
                    {
                        method: 'GET',
                        isArray: true,
                        transformResponse: function(response) {
                            var newData = JSON.parse(response);

                            newData.map(function(data) {
                                data._length = data.length;
                                return data;
                            });

                            return newData;
                        }
                    }
                }
            );
        }
    }
)
();
