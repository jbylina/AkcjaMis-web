(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyDetailController', FamilyDetailController);

    FamilyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Family', 'Contact', 'Child', 'FamilyNote', 'ChristmasPackage'];

    function FamilyDetailController($scope, $rootScope, $stateParams, entity, Family, Contact, Child, FamilyNote, ChristmasPackage) {
        var vm = this;
        vm.family = entity;
        vm.contacts = [
            {
                id:1,
                type:'email',
                value:'family3@mis.pl'
            },
            {
                id:2,
                type:'mobile',
                value:'123-456-789'
            }
        ];
        vm.childrens = [
            {
                id:1,
                first_name:'Jaś',
                last_name:'Kowalski',
                sex:'MALE',
                birth_year:'2006'
            },
            {
                id:2,
                first_name:'Małgosia',
                last_name:'Kowalska',
                sex:'FEMALE',
                birth_year:'2008'
            }

        ];

        vm.familyNotes = [
            {
                id:1,
                content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse rhoncus ex vel scelerisque suscipit. Proin a elementum urna. Nulla facilisi. Pellentesque vehicula leo metus, sed facilisis ipsum fermentum et. Aenean feugiat nibh vitae commodo aliquam. Mauris pharetra posuere sapien, non mollis lorem ultrices a. Nam odio orci, fermentum nec dignissim tincidunt, lobortis at metus. Vivamus condimentum purus non lobortis auctor. Phasellus semper quis nunc non placerat. Curabitur pretium est quis nulla fringilla, vitae vehicula sapien fringilla.',
                created_by: 'admin',
                created_date: '05-05-2016 17:18:00'
            },
            {
                id:2,
                content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse rhoncus ex vel scelerisque suscipit. Proin a elementum urna. Nulla facilisi. Pellentesque vehicula leo metus, sed facilisis ipsum fermentum et. Aenean feugiat nibh vitae commodo aliquam. Mauris pharetra posuere sapien, non mollis lorem ultrices a. Nam odio orci, fermentum nec dignissim tincidunt, lobortis at metus. Vivamus condimentum purus non lobortis auctor. Phasellus semper quis nunc non placerat. Curabitur pretium est quis nulla fringilla, vitae vehicula sapien fringilla.',
                created_by: 'mlagod',
                created_date: '02-04-2016 14:28:50'
            }
        ];

        vm.load = function (id) {
            Family.get({id: id}, function(result) {
                vm.family = result;
            });

            Contact.get({family_id: id}, function(result){
                vm.contact = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:familyUpdate', function(event, result) {
            vm.family = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
