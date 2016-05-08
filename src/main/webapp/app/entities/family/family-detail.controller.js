(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyDetailController', FamilyDetailController);

    FamilyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Family', 'Contact', 'Child', 'FamilyNote', 'ChristmasPackage', '$http', '$resource'];

    function FamilyDetailController($scope, $rootScope, $stateParams, entity, Family, Contact, Child, FamilyNote, ChristmasPackage, $http, $resource) {
        var vm = this;
        vm.family = entity;

        vm.packages = [
            {
                id:1,
                mark: '5',
                year: '2011'
            },
            {
                id:2,
                mark:'2',
                year: '2012'
            },
            {
                id:3,
                mark:'4',
                year: '2013'
            },
            {
                id:5,
                mark:'1',
                year: '2015'
            }

        ];

        vm.familyNotes = $resource('api/families/:id/family-notes', {id:$stateParams.id}).query();
        vm.childrens = $resource('/api/families/:id/children', {id:$stateParams.id}).query();
        vm.contacts = $resource('/api/families/:id/contacts', {id:$stateParams.id}).query();


        vm.load = function (id) {
            Family.get({id: id}, function(result) {
                vm.family = result;
            });

        };
        var unsubscribe = $rootScope.$on('akcjamisApp:familyUpdate', function(event, result) {
            vm.family = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
