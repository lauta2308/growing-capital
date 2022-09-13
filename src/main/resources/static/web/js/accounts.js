const { createApp } = Vue

createApp({
    data() {
        return {

            data: [],
            urlTransaction: "http://localhost:8080/web/account.html",
            dataLoans: "",
            numberAccountTransaction: "",
            cuentaSeleccionada: [],
            hasLoans: false,
            cantidadPrestamosCliente: "",
            cuentasActivas: [],
            cantidadCuentas: "",
            puedeSolicitarPrestamo: "",


        }
    },
    created() {
        this.loadData();
        this.verificarLoans();

    },
    methods: {
        async loadData() {

            await axios.get("/api/clients/current").then(response => {

                this.data = response.data;
                this.cuentasActivas = this.data.account.filter(account => account.status === "ACTIVE");
                this.cuentasActivas = this.cuentasActivas.sort(function(a, b) { return a.id - b.id });
                this.cantidadCuentas = this.cuentasActivas.length;
                this.cantidadPrestamosCliente = this.data.loans;


                if (this.data.loans) {
                    if (this.data.loans.length > 0) {
                        this.hasLoans = true;



                    }
                }





            })

        },
        async verificarLoans() {

            await axios.get("/api/loans").then(response => {

                this.dataLoans = response.data;

                this.compararLoans();

            })

            .catch(error => console.log(error));

        },
        compararLoans: function() {
            if (this.cantidadPrestamosCliente.length < this.dataLoans.length) {
                this.puedeSolicitarPrestamo = "si";

            }

        },

        transaccionesDeCuenta: function(id, numero) {


            for (let i = 0; i < this.data.account.length; i++) {
                if (this.data.account[i].id === id) {
                    this.urlTransaction = `./account.html?id=${id}`
                    this.numberAccountTransaction = numero;

                }

                localStorage.setItem("numeroCuentaTransaccion", JSON.stringify(numero));




            }


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
            mesFecha = new Date(date).getMonth() + 1;
            return anioFecha + " - " + mesFecha;

        },
        ocultarForm: function() {
            var loginFormContainer = document.querySelector(".login-form-container");

            loginFormContainer.style.display = "none";


        },
        cerrarSesion: function() {
            axios.post('/api/logout').then(response => window.location.href = "../index.html")
        },

        mostrarCuenta: function(cuenta) {

            let barraDerCuenta = document.getElementById("barra-der-cuenta");
            barraDerCuenta.style.display = "flex"
            this.cuentaSeleccionada = cuenta;
        },


        desplegarCuentas: function() {
            let resumenCuenta = document.querySelector(".contenedor-resumen-cuentas");
            let iconoDesplegarCuenta = document.querySelector(".desplegar-cuentas-on");
            let iconoOcultarCuenta = document.querySelector(".desplegar-cuentas-off");

            resumenCuenta.style.display = "flex";
            iconoDesplegarCuenta.style.display = "none";
            iconoOcultarCuenta.style.display = "block";

        },

        ocultarCuentas: function() {
            let resumenCuenta = document.querySelector(".contenedor-resumen-cuentas");
            let iconoDesplegarCuenta = document.querySelector(".desplegar-cuentas-on");
            let iconoOcultarCuenta = document.querySelector(".desplegar-cuentas-off");

            resumenCuenta.style.display = "none";
            iconoDesplegarCuenta.style.display = "block";
            iconoOcultarCuenta.style.display = "none";
        },

        desplegarPrestamos: function() {
            let contenedorResumentPrestamos = document.querySelector(".contenedor-resumen-prestamos");
            let iconoDesplegarPrestamos = document.querySelector(".desplegar-prestamos-on");
            let iconoOcultarPrestamos = document.querySelector(".desplegar-prestamos-off");

            contenedorResumentPrestamos.style.display = "flex";
            iconoDesplegarPrestamos.style.display = "none";
            iconoOcultarPrestamos.style.display = "block";

        },

        ocultarPrestamos: function() {
            let contenedorResumentPrestamos = document.querySelector(".contenedor-resumen-prestamos");
            let iconoDesplegarPrestamos = document.querySelector(".desplegar-prestamos-on");
            let iconoOcultarPrestamos = document.querySelector(".desplegar-prestamos-off");

            contenedorResumentPrestamos.style.display = "none";
            iconoDesplegarPrestamos.style.display = "block";
            iconoOcultarPrestamos.style.display = "none";
        },

        solicitudEliminarCuenta: function(cuenta) {
            let parrafoEliminarCuenta = document.querySelector(".eliminar-cuenta-error");


            if (cuenta.balance > 0) {
                parrafoEliminarCuenta.innerText = "No puedes eliminar una cuenta con balance superior a 0"
                parrafoEliminarCuenta.style.display = "block";
            } else {
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
            }



        },

        eliminarCuenta: function(cuenta) {
            axios.patch("/api/clients/current/accounts/delete", {


                    "id": cuenta.id,
                    "number": cuenta.number,
                    "accountType": cuenta.accountType,
                    "creationDate": cuenta.creationDate,
                    "balance": cuenta.balance,
                    "card": cuenta.card,
                    "transactions": cuenta.transactions,
                    "status": cuenta.status,


                }).then(Swal.fire(
                    'Excelente!',
                    'Cuenta eliminada con éxito',
                    'success'
                ))
                .then(
                    setTimeout(function() {
                        window.location.href = "./accounts.html"
                    }, 1500))

            .catch(err => {

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