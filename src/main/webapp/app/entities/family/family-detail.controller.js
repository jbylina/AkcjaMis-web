(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyDetailController', FamilyDetailController);

    FamilyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', '$state', 'entity', 'Family', 'Contact', 'Child', 'FamilyNote', 'ChristmasPackage', '$http', '$resource', '$filter'];

    function FamilyDetailController($scope, $rootScope, $stateParams, $state, entity, Family, Contact, Child, FamilyNote, ChristmasPackage, $http, $resource, $filter) {
        var vm = this;
        vm.family = entity;

        vm.sexes = [
            {value: 'FEMALE', text: 'kobieta'},
            {value: 'MALE', text: 'mężczyzna'}
        ];

        vm.sexesMap = {
            'FEMALE': 'kobieta',
            'MALE': 'mężczyzna'
        };

        vm.contactsMap = {
            'MAIL': 'E-mail:',
            'TEL': 'Tel. kont. :'
        };

        vm.contactsVal = [
            {value: 'MAIL', text: 'E-mail'},
            {value: 'TEL', text: 'Tel. kont'}
        ];

        vm.packages = Family.getPackages({id: $stateParams.id});
        vm.familyNotes = FamilyNote.query({id:$stateParams.id});
        vm.childrens = Child.query({id:$stateParams.id});
        vm.contacts = Contact.query({id:$stateParams.id});



        var unsubscribe = $rootScope.$on('akcjamisApp:familyUpdate', function(event, result) {
            vm.family = result;
        });
        $scope.$on('$destroy', unsubscribe);

        var onSaveSuccess = function (result) {
        };

        var onSaveNoteSuccess = function (result) {

            vm.familyNotes.push(result);

        };

        var onSaveError = function () {
            $state.go('family-detail', {id: vm.family.id} , { reload: true });
        };

        vm.save = function () {

            vm.isSaving = true;

            vm.family = Family.update(vm.family, onSaveSuccess, onSaveError);

            vm.contacts.forEach(function(contact) {
                Contact.update({id : vm.family.id}, contact, onSaveSuccess, onSaveError);
            });

            vm.childrens.forEach(function(child) {
                Child.update({id : vm.family.id}, child, onSaveSuccess, onSaveError);
            });

            vm.familyNotes.forEach(function(note) {
                if (note.id)
                    FamilyNote.update({id : vm.family.id}, note, onSaveSuccess, onSaveError);
                else {
                    FamilyNote.save({id: vm.family.id}, note, onSaveNoteSuccess, onSaveError);
                }
            });

            if ($scope.contentModel)
                vm.addNote();

            vm.isSaving = false;
        };

        vm.deleteChild = function (id) {

            if(id == null) {
                vm.childrens.pop();
                return;
            }

            var filtered = $filter('filter')(vm.childrens, {id: id});

            var index = vm.childrens.indexOf(filtered[0]);
            vm.childrens.splice(index, 1);

            Child.delete({familyId : vm.family.id, id: id}, onSaveSuccess, onSaveError);

        }

        vm.addChild = function () {
            vm.childrens.push(new Child);
        }

        vm.deleteNote = function (id) {

            if(id == null) {
                vm.familyNotes.pop();
                return;
            }

            var filtered = $filter('filter')(vm.familyNotes, {id: id});

            var index = vm.familyNotes.indexOf(filtered[0]);
            vm.familyNotes.splice(index, 1);

            FamilyNote.delete({familyId : vm.family.id, id: id}, onSaveSuccess, onSaveError);

        }

        vm.addNote = function () {
            var note = new FamilyNote;
            note.content = $scope.contentModel;
            FamilyNote.save({id: vm.family.id}, note, onSaveNoteSuccess, onSaveError);
            $scope.contentModel = '';
        }

        vm.addContact = function () {
            vm.contacts.push(new Contact);
        }

        vm.deleteContact = function (id) {

            if(id == null) {
                vm.contacts.pop();
                return;
            }

            var filtered = $filter('filter')(vm.contacts, {id: id});

            var index = vm.contacts.indexOf(filtered[0]);
            vm.contacts.splice(index, 1);

            Contact.delete({familyId : vm.family.id, id: id}, onSaveSuccess, onSaveError);
        }
    }
})();
