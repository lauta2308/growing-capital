const { createApp } = Vue

createApp({
    data() {
        return {

            data: [],
            cards: [],
            cardDate: "",
            cantidadTarjetasDebitoCliente: "",
            vencimientoTarjeta: "",




        }
    },
    created() {
        this.loadData();

    },
    methods: {
        async loadData() {

            await axios.get("/api/clients/current").then(response => {

                this.data = response.data;
                this.cards = this.data.cards.filter(card => card.status === "ACTIVE");
                this.cards = this.cards.sort(function(a, b) { return a.id - b.id });

                let cantidadTarjetasCredito = this.cards.filter(card => card.status === 'ACTIVE' && card.type === 'CREDITO');
                this.cantidadTarjetasCreditoCliente = cantidadTarjetasCredito.length;




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
            mesFecha = new Date(date).getMonth() + 1;


            let fechaActual = new Date();
            let anioActual = fechaActual.getFullYear();
            let mesActual = fechaActual.getMonth() + 1;


            if (anioFecha < anioActual) {
                this.vencimientoTarjeta = "Vencida"
            } else if (anioFecha === anioActual && mesFecha < mesActual) {
                this.vencimientoTarjeta = "Vencida"
            } else if (anioFecha === anioActual && mesFecha < mesActual <= 6) {
                this.vencimientoTarjeta = "Próxima a vencer"
            } else {
                this.vencimientoTarjeta = "";
            }
            return anioFecha + " - " + mesFecha;

        },

        solicitudEliminarCard: function(card) {
            Swal.fire({
                title: '¿Eliminar Tarjeta?',
                text: "Se eliminará la tarjeta",
                icon: 'warning',
                showCancelButton: true,
                cancelButtonText: "Cancelar",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, eliminar'
            }).then((result) => {
                if (result.isConfirmed) {

                    this.eliminarTarjeta(card);
                }
            })
        },

        eliminarTarjeta(card) {

            axios.patch("/api/clients/current/accounts/cards/delete", {

                    "id": card.id,
                    "type": card.type,
                    "color": card.color,
                    "number": card.number,
                    "cvv": card.cvv,
                    "fromDate": card.fromDate,
                    "thruDate": card.thruDate,
                    "cardHolder": card.cardHolder,
                    "status": card.status

                }).then(Swal.fire(
                    'Excelente!',
                    'Tarjeta eliminada con éxito',
                    'success'
                ))
                .then(
                    setTimeout(function() {
                        window.location.href = "./cards.html"
                    }, 1500))

            .catch(err => {

                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: err.response.data,


                })
            })

        },
        solicitudVerCuenta: function(card) {

            let cuentaAsociada = this.data.account.filter(account => account.card.number === card.number)


            localStorage.setItem("numeroCuentaTransaccion", JSON.stringify(cuentaAsociada[0].number));


            window.location.href = `./account.html?id=${cuentaAsociada[0].id}`;



        },


    },

    computed: {


    }
}).mount('#app')