(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageNoteDetailController', PackageNoteDetailController);

    PackageNoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PackageNote', 'PackageGift'];

    function PackageNoteDetailController($scope, $rootScope, $stateParams, entity, PackageNote, PackageGift) {
        var vm = this;
        vm.packageNote = entity;
        vm.load = function (id) {
            PackageNote.get({id: id}, function(result) {
                vm.packageNote = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:packageNoteUpdate', function(event, result) {
            vm.packageNote = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
