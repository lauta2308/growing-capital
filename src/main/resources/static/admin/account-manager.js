const { createApp } = Vue


createApp({
    data() {
        return {
            cuentasPendientesDeEliminar: [],


        }
    },
    created() {

        this.loadAccountsInfo();

    },
    methods: {
        async loadAccountsInfo() {


            await axios.get("/api/accounts").then(response => {
                this.cuentasPendientesDeEliminar = response.data.filter(account => account.status === 'PENDINGDELETE');
                this.cuentasPendientesDeEliminar = this.cuentasPendientesDeEliminar.sort(function(a, b) { return a.id - b.id });

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

        solicitudEliminarCuenta: function(cuenta) {


            Swal.fire({
                title: '¿Eliminar Cuenta?',
                text: "Se eliminará la cuenta",
                icon: 'warning',
                showCancelButton: true,
                cancelButtonText: "Cancelar",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, eliminar'
            }).then((result) => {
                if (result.isConfirmed) {

                    this.eliminarCuenta(cuenta);
                }
            })
        },

        eliminarCuenta(cuenta) {



            axios.delete("/api/clients/current/accounts/delete", {


                    data: {
                        "id": cuenta.id,
                        "number": cuenta.number,
                        "accountType": cuenta.accountType,
                        "creationDate": cuenta.creationDate,
                        "balance": cuenta.balance,
                        "card": cuenta.card,
                        "transactions": cuenta.transactions,
                        "status": cuenta.status

                    }



                }).then(Swal.fire(
                    'Excelente!',
                    'Cuenta eliminada con éxito',
                    'success'
                ))
                .then(
                    setTimeout(function() {
                        window.location.href = "./account-manager.html"
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