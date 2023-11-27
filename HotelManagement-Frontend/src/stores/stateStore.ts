import {defineStore} from 'pinia'
import Cookies from 'js-cookie'
import {jwtDecode} from "jwt-decode";

export const useStore = defineStore('loginStore', {
    state: () => {
        const token = Cookies.get('token')
        return {login: token != null, role: token!=null?jwtDecode(token)['role']:undefined}
    }, actions: {
        changeToLogin() {
            const token = Cookies.get('token')
            this.login = true
            this.role = jwtDecode(token)['role']
        },
        changeToLogout(){
            this.login = false
            this.role = undefined
        }
    }
})
