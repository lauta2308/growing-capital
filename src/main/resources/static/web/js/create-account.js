const { createApp } = Vue

createApp({
    data() {
        return {
            accounts: [],
            cards: [],
            cantidadTarjetasDebito: 0,
            cantidadTarjetasDebitoGold: 0,
            cantidadTarjetasDebitoSilver: 0,
            cantidadTarjetasDebitoTitanium: 0,
            tipoCuentaSeleccionada: "",
            colorTarjetaSeleccionada: ""








        }
    },
    created() {
        this.loadClientAccounts();
        this.loadClientCards();


    },
    methods: {
        async loadClientAccounts() {

            await axios.get("/api/clients/current/accounts").then(response => {

                this.accounts = response.data.filter(account => account.status === "ACTIVE");




            })


        },
        async loadClientCards() {

            await axios.get("/api/clients/current/cards").then(response => {

                this.cards = response.data;



                for (let i = 0; i < this.cards.length; i++) {
                    if (this.cards[i].type === "DEBITO" && this.cards[i].status === "ACTIVE") {
                        this.cantidadTarjetasDebito = this.cantidadTarjetasDebito + 1;
                        if (this.cards[i].color === "GOLD") {
                            this.cantidadTarjetasDebitoGold = this.cantidadTarjetasDebitoGold + 1
                        } else if (this.cards[i].color === "SILVER") {
                            this.cantidadTarjetasDebitoSilver = this.cantidadTarjetasDebitoSilver + 1
                        } else {
                            this.cantidadTarjetasDebitoTitanium = this.cantidadTarjetasDebitoTitanium + 1
                        }
                    }

                }


            })


        },

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
        aparecerLogout: function() {
            Swal.fire({
                title: '¿Cerrar sesión?',
                text: "Volverás al index",
                icon: 'warning',
                showCancelButton: true,
                cancelButtonText: "Cancelar",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, cerrar sesión'
            }).then((result) => {
                if (result.isConfirmed) {

                    this.cerrarSesion();
                }
            })
        },

        ocultarForm: function() {
            var loginFormContainer = document.querySelector(".login-form-container");

            loginFormContainer.style.display = "none";


        },
        cerrarSesion: function() {
            axios.post('/api/logout').then(response => window.location.href = "./index.html")
        },

        solicitudAgregarCuenta() {
            if (this.accounts.length >= 3) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "No puedes crear mas de 3 cuentas"


                })

            } else if (this.cards.includes(this.colorTarjetaSeleccionada)) {

                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Ya tienes una tarjeta del mismo color y tipo, elige otra"


                })
            } else {
                Swal.fire({
                    title: '¿Agregar cuenta?',
                    text: "Se agregará la cuenta",
                    icon: 'warning',
                    showCancelButton: true,
                    cancelButtonText: "Cancelar",
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Si, agregar'
                }).then((result) => {
                    if (result.isConfirmed) {

                        this.agregarCuenta();
                    }
                })

            }

        },
        agregarCuenta() {


            axios.post("/api/clients/current/accounts", `accountType=${this.tipoCuentaSeleccionada}&cardColor=${this.colorTarjetaSeleccionada}`, {
                    headers: { 'content-type': 'application/x-www-form-urlencoded' }
                })
                .then(Swal.fire(
                    'Excelente!',
                    'Se te agregó una nueva cuenta..',
                    'success'
                ))
                .then(
                    setTimeout(function() {
                        window.location.href = "./accounts.html"
                    }, 1500))

            .catch(err => {
                console.log(err.response.data);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: err.response.data,


                })




            })


        }







    },

    computed: {


    }
}).mount('#app')