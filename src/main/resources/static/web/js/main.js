const { createApp } = Vue

createApp({
    data() {
        return {
            newUserName: "",
            newUserLastName: "",
            newUserEmail: "",
            newUserPassword: "",
            userEmail: "",
            userPassword: ""



        }
    },
    created() {




    },
    methods: {



        abrirMenu: function() {
            let iconoAbrirMenu = document.getElementById("open-menu-icon");
            let iconoCerrarMenu = document.getElementById("close-menu-icon");
            let navegacion = document.getElementById("navegacion");

            iconoAbrirMenu.style.display = "none";
            iconoCerrarMenu.style.display = "block";
            navegacion.style.display = "flex";
        },

        cerrarMenu: function() {
            let iconoAbrirMenu = document.getElementById("open-menu-icon");
            let iconoCerrarMenu = document.getElementById("close-menu-icon");
            let navegacion = document.getElementById("navegacion");

            iconoAbrirMenu.style.display = "block";
            iconoCerrarMenu.style.display = "none";
            navegacion.style.display = "none";

        },

        aparecerFormularioRegistro: function() {
            var loginFormContainer = document.querySelector(".login-form-container");
            var registerForm = document.querySelector(".contenedor-titulo-registro-form");
            var loginForm = document.querySelector(".contenedor-titulo-iniciarSesion-form");

            loginFormContainer.style.display = "block";
            loginForm.style.display = "none";
            registerForm.style.display = "block";

        },
        aparecerFormLogin: function() {
            var registerForm = document.querySelector(".contenedor-titulo-registro-form");
            var loginForm = document.querySelector(".contenedor-titulo-iniciarSesion-form");

            registerForm.style.display = "none";
            loginForm.style.display = "block";
        },

        ocultarForm: function() {
            var loginFormContainer = document.querySelector(".login-form-container");

            loginFormContainer.style.display = "none";


        },
        nuevoRegistro: function() {
            var registroCorrectoMsg = document.querySelector(".registro-correcto")
            var registroIncorrectoMsg = document.querySelector(".registro-incorrecto")

            registroCorrectoMsg.style.display = "none"
            registroIncorrectoMsg.style.display = "none"

            if (!this.newUserEmail.includes("@")) {
                registroIncorrectoMsg.style.display = "block"
            } else {
                axios.post('/api/clients', `name=${this.newUserName}&lastName=${this.newUserLastName}&email=${this.newUserEmail}&password=${this.newUserPassword}`)
                    .then(response => {
                        registroCorrectoMsg.style.display = "block"
                        this.validarRegistro(this.newUserEmail, this.newUserPassword)

                    })
                    .catch(err => {

                        registroIncorrectoMsg.style.display = "block"
                        this.newUserName = ""
                        this.newUserLastName = ""
                        this.newUserEmail = ""
                        this.newUserPassword = ""
                        let inputs = document.querySelectorAll("input[type = text],  input[type = email], input[type = password]")

                        inputs.forEach(input => input.value = "")

                    })

            }



        },

        validarRegistro: function(email, password) {

            var registroCorrectoMsg = document.querySelector(".registro-correcto")
            var registroIncorrectoMsg = document.querySelector(".registro-incorrecto")
            var loginCorrectoMsg = document.querySelector(".login-correcto")
            var loginIncorrectoMsg = document.querySelector(".login-incorrecto")
            loginCorrectoMsg.style.display = "none"
            loginIncorrectoMsg.style.display = "none"

            axios.post('/api/login', `email=${email}&password=${password}`)
                .then(response => {
                    registroCorrectoMsg.style.display = "none"
                    loginCorrectoMsg.style.display = "block"
                    if (email.includes("admin")) {

                        window.location.href = "./h2-console/"
                    } else {
                        window.location.href = "./web/accounts.html"
                    }
                    this.newUserName = ""
                    this.newUserLastName = ""
                    this.newUserEmail = ""
                    this.newUserPassword = ""
                    let inputs = document.querySelectorAll("input[type = text],  input[type = email], input[type = password]")

                    inputs.forEach(input => input.value = "")

                })
                .catch(error => {

                    loginIncorrectoMsg.style.display = "block"
                })


        },




        validarDatos: function() {
            var loginCorrectoMsg = document.querySelector(".login-correcto")
            var loginIncorrectoMsg = document.querySelector(".login-incorrecto")

            loginCorrectoMsg.style.display = "none"
            loginIncorrectoMsg.style.display = "none"
            axios.post('/api/login', `email=${this.userEmail}&password=${this.userPassword}`)
                .then(response => {


                    loginCorrectoMsg.style.display = "block"

                    if (this.userEmail.includes("admin")) {
                        window.location.href = "./admin/manager.html"
                    } else {
                        window.location.href = "./web/accounts.html"
                    }


                })
                .catch(error => {
                    console.log(error);
                    loginIncorrectoMsg.style.display = "block"
                })


        },

    },

    computed: {




    }
}).mount('#app')