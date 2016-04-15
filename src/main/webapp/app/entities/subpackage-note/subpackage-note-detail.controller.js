(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SubpackageNoteDetailController', SubpackageNoteDetailController);

    SubpackageNoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SubpackageNote', 'Sybpackage'];

    function SubpackageNoteDetailController($scope, $rootScope, $stateParams, entity, SubpackageNote, Sybpackage) {
        var vm = this;
        vm.subpackageNote = entity;
        vm.load = function (id) {
            SubpackageNote.get({id: id}, function(result) {
                vm.subpackageNote = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:subpackageNoteUpdate', function(event, result) {
            vm.subpackageNote = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
