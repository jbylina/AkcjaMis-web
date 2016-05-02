(function () {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$location', '$state', 'Principal'];

    function SidebarController($location, $state, Principal) {
        var sb = this;

        sb.navCollapsed = true;
        sb.isAuthenticated = Principal.isAuthenticated;
        sb.$state = $state;

        sb.selectedMenu = 'dashboard';
        sb.collapseVar = 0;
        sb.multiCollapseVar = 0;

        sb.check = function (x) {
            if (x == sb.collapseVar)
                sb.collapseVar = 0;
            else
                sb.collapseVar = x;
        };

        sb.multiCheck = function (y) {
            if (y == sb.multiCollapseVar)
                sb.multiCollapseVar = 0;
            else
                sb.multiCollapseVar = y;
        };
    }
})();
