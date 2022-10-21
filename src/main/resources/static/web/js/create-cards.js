const { createApp } = Vue

createApp({
    data() {
        return {

            data: [],
            cards: [],
            cardDate: "",
            cantidadTarjetasCliente: 0,
            cantidadTarjetasCredito: 0,
            cantidadTarjetasCreditoGold: 0,
            cantidadTarjetasCreditoSilver: 0,
            cantidadTarjetasCreditoTitanium: 0,
            tipoTarjetaSeleccionada: "",
            colorTarjetaSeleccionada: "",



        }
    },
    created() {
        this.loadData();

    },
    methods: {
        async loadData() {

            await axios.get("/api/clients/current").then(response => {
                this.data = response.data;
                this.cards = this.data.cards;


                for (let i = 0; i < this.cards.length; i++) {
                    if (this.cards[i].type === "CREDITO" && this.cards[i].status === "ACTIVE") {
                        this.cantidadTarjetasCredito = this.cantidadTarjetasCredito + 1
                        if (this.cards[i].color === "GOLD") {
                            this.cantidadTarjetasCreditoGold = this.cantidadTarjetasCreditoGold + 1
                        } else if (this.cards[i].color === "SILVER") {
                            this.cantidadTarjetasCreditoSilver = this.cantidadTarjetasCreditoSilver + 1
                        } else {
                            this.cantidadTarjetasCreditoTitanium = this.cantidadTarjetasCreditoTitanium + 1

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
        accountDateFormat: function(date) {
            anioFecha = new Date(date).getFullYear();
            mesFecha = new Date(date).getMonth();
            return anioFecha + " - " + mesFecha;

        },
        ocultarForm: function() {
            var loginFormContainer = document.querySelector(".login-form-container");

            loginFormContainer.style.display = "none";


        },
        cerrarSesion: function() {
            axios.post('/api/logout').then(response => window.location.href = "../index.html")
        },
        accountDateFormat: function(date) {
            anioFecha = new Date(date).getFullYear();
            mesFecha = new Date(date).getMonth();
            return anioFecha + " - " + mesFecha;

        },
        solicitudAgregarTarjeta: function() {

            if (this.tipoTarjetaSeleccionada == "" || this.colorTarjetaSeleccionada == "") {
                document.querySelector(".create-cards-error").style.display = "block";

            } else {
                Swal.fire({
                    title: '¿Crear Tarjeta?',
                    text: "Se creará la tarjeta",
                    icon: 'warning',
                    showCancelButton: true,
                    cancelButtonText: "Cancelar",
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Si, crear'
                }).then((result) => {
                    if (result.isConfirmed) {

                        this.crearTarjeta();
                    }
                })

            }
        },

        crearTarjeta: function() {
            axios.post("/api/clients/current/cards", `type=${this.tipoTarjetaSeleccionada}&color=${this.colorTarjetaSeleccionada}`)
                .then(Swal.fire(
                    'Excelente!',
                    'Se agregó una nueva tarjeta..',
                    'success'
                ))
                .then(
                    setTimeout(function() {
                        window.location.href = "./cards.html"
                    }, 1500))

            .catch(error => console.log(error))
        }








    },

    computed: {


    }
}).mount('#app')