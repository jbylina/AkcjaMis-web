(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyNoteDetailController', FamilyNoteDetailController);

    FamilyNoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'FamilyNote', 'Tag', 'Family'];

    function FamilyNoteDetailController($scope, $rootScope, $stateParams, entity, FamilyNote, Tag, Family) {
        var vm = this;
        vm.familyNote = entity;
        vm.load = function (id) {
            FamilyNote.get({id: id}, function(result) {
                vm.familyNote = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:familyNoteUpdate', function(event, result) {
            vm.familyNote = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
