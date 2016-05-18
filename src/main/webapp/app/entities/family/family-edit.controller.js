/**
 * Created by mlagod on 16.05.16.
 */
(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyEditController', FamilyEditController);

    FamilyEditController.$inject = ['$stateParams', 'entity', 'Family', 'FamilyNote'];

    function FamilyEditController($stateParams, entity, Family, $resource){

        var vm = this;
        vm.family = entity;
        vm.familyNotes = $resource('/api/families/:id/family-notes', {id:$stateParams.id}).query();
        vm.childrens = $resource('/api/families/:id/children', {id:$stateParams.id}).query();
        vm.contacts = $resource('/api/families/:id/contacts', {id:$stateParams.id}).query();


        vm.load = function(id){
            Family.get({id: id}, function(result) {
                vm.family = result;
            });
        }
    }
});
