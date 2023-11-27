import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MainPage from "@/components/MainPage.vue";
import RoomsPage from "@/components/RoomsPage.vue";
import ContactPage from "@/components/ContactPage.vue";
import ResultPage from "@/components/ResultPage.vue";
import AccountPage from "@/components/AccountPage.vue";
import InfoPage from "@/components/InfoPage.vue";
import ReservationsPage from "@/components/ReservationsPage.vue";
import ManagementPage from "@/components/ManagementPage.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView,
            children: [
                {
                    path: '',
                    name: 'main',
                    component: MainPage
                },
                {
                    path: 'rooms',
                    name: 'rooms',
                    component: RoomsPage
                },
                {
                    path: 'contact',
                    name: 'contact',
                    component: ContactPage
                },
                {
                    path: 'result',
                    name: 'result',
                    component: ResultPage
                },
                {
                    path: 'my-account',
                    name: 'account',
                    component: AccountPage,
                    children: [
                        {
                            path: '',
                            name: 'info',
                            component: InfoPage
                        },
                        {
                            path: 'reservations',
                            name: 'reservations',
                            component: ReservationsPage
                        }
                    ]
                },
                {
                    path: 'management',
                    name: 'management',
                    component: ManagementPage,
                }
            ]
        },

    ]
})

export default router
