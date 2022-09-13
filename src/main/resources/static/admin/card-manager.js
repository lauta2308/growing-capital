const { createApp } = Vue


createApp({
    data() {
        return {
            tarjetasPendientesDeEliminar: [],


        }
    },
    created() {

        this.loadCardsInfo();

    },
    methods: {
        async loadCardsInfo() {


            await axios.get("/api/cards").then(response => {
                this.tarjetasPendientesDeEliminar = response.data.filter(tarjeta => tarjeta.status === 'INACTIVE');
                this.tarjetasPendientesDeEliminar = this.tarjetasPendientesDeEliminar.sort(function(a, b) { return a.id - b.id });


            })

            .catch(error => console.log(error));
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
            axios.post('/api/logout').then(response => window.location.href = "../index.html")
        },

        cuentaDateFormat: function(date) {

            let fecha = new Date(date);
            anioFecha = fecha.getFullYear();
            mesFecha = fecha.getMonth() + 1;

            return anioFecha + " - " + mesFecha;

        },

        solicitudEliminarTarjeta: function(tarjeta) {


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

                    this.eliminarCuenta(tarjeta);
                }
            })
        },

        eliminarCuenta(tarjeta) {



            axios.delete("/api/cards/delete", {


                    data: {
                        "id": tarjeta.id,
                        "type": tarjeta.type,
                        "color": tarjeta.color,
                        "number": tarjeta.number,
                        "cvv": tarjeta.cvv,
                        "fromDate": tarjeta.fromDate,
                        "thruDate": tarjeta.thruDate,
                        "cardHolder": tarjeta.cardHolder,
                        "status": tarjeta.status

                    }



                }).then(Swal.fire(
                    'Excelente!',
                    'Tarjeta eliminada con éxito',
                    'success'
                ))
                .then(
                    setTimeout(function() {
                        window.location.href = "./card-manager.html"
                    }, 1500))

            .catch(err => {
                console.log(err.response.data);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: err.response.data,


                })
            })

        },


    },
    computed: {}
}).mount('#app')