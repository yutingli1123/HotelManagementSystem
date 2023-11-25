import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MainPage from "@/components/MainPage.vue";
import RoomsPage from "@/components/RoomsPage.vue";
import ContactPage from "@/components/ContactPage.vue";

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
            ]
        },
    ]
})

export default router
