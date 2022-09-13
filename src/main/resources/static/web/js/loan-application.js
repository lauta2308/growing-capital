const { createApp } = Vue

createApp({
    data() {
        return {
            userInfo: "",
            dataLoans: "",
            userAccounts: [],
            userLoans: [],
            loanOptions: [],
            tipoPrestamoSeleccionado: "",
            idPrestamoSeleccionado: "", //convertir el tipo de préstamo a un número (id) para poder enviar el post
            pagosDisponiblesPrestamoSeleccionado: "",
            pagosSeleccionados: "",
            maximoMontoPrestamo: "",
            montoSolicitado: "",
            cuentaDestino: "",
            feePrestamo: "",
            montoDeCuota: "",



        }
    },
    created() {
        this.loadUserInfo();
        this.loadLoansInfo();

    },
    methods: {
        async loadUserInfo() {

            await axios.get("/api/clients/current").then(response => {
                this.userInfo = response.data
                this.userLoans = this.userInfo.loans
                this.userAccounts = this.userInfo.account.filter(account => account.status === "ACTIVE");








            })
        },

        async loadLoansInfo() {


            await axios.get("/api/loans").then(response => {

                this.dataLoans = response.data;

                this.verifyUserInfo();

            })

            .catch(error => console.log(error));


        },
        verifyUserInfo: function() {
            for (let i = 0; i < this.dataLoans.length; i++) {
                if (this.userLoans.filter(loan => loan.loanName === this.dataLoans[i].name).length === 0) {
                    this.loanOptions.push(this.dataLoans[i].name);
                }
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

        ocultarForm: function() {
            var loginFormContainer = document.querySelector(".login-form-container");

            loginFormContainer.style.display = "none";


        },
        cerrarSesion: function() {
            axios.post('/api/logout').then(response => window.location.href = "../index.html")
        },

        mostrarPagosDisponibles: function(event) {

            for (let i = 0; i < this.dataLoans.length; i++) {
                if (this.dataLoans[i].name === event.target.value) {


                    this.pagosDisponiblesPrestamoSeleccionado = this.dataLoans[i].payments;

                    this.maximoMontoPrestamo = this.dataLoans[i].maxAmmount;
                    this.idPrestamoSeleccionado = this.dataLoans[i].id;
                    this.feePrestamo = this.dataLoans[i].fee;

                }

            }




        },

        agregarPrestamo: function() {

            let cuentaDestinoValida = false;
            for (let i = 0; i < this.userAccounts.length; i++) {

                if (this.userAccounts[i].number === this.cuentaDestino) {
                    cuentaDestinoValida = true;
                    break;
                }
            }


            let mensajeError = document.querySelector(".create-loan-error");

            if (this.tipoPrestamoSeleccionado === "" || this.pagosSeleccionados === "" || this.montoSolicitado === "" || this.cuentaDestino === "") {
                mensajeError.style.display = "block";
            } else if (this.montoSolicitado < 20000 || this.montoSolicitado > this.maximoMontoPrestamo) {
                let ammountError = document.querySelector(".loan-ammount-error")

                ammountError.style.display = "block";
            } else if (!cuentaDestinoValida) {
                destinyAccountError = document.querySelector(".destiny-account-error")

                destinyAccountError.style.display = "block";
            } else {

                Swal.fire({
                    title: '¿Solicitar Préstamo?',
                    text: "Se agregará el préstamo solicitado",
                    icon: 'warning',
                    showCancelButton: true,
                    cancelButtonText: "Cancelar",
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Si, solicitar'
                }).then((result) => {
                    if (result.isConfirmed) {

                        this.confirmarPrestamo()
                    }
                })


            }

        },

        confirmarPrestamo: function() {
            axios.post("/api/clientloans", {
                    "loan": this.idPrestamoSeleccionado,
                    "amount": this.montoSolicitado,
                    "payments": this.pagosSeleccionados,
                    "destinyAccount": this.cuentaDestino
                }).then(Swal.fire(
                    'Excelente!',
                    'Préstamo agregado con éxito',
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


        montoCuotas() {

            let montoDeCuota = this.montoSolicitado / this.pagosSeleccionados;

            if (montoDeCuota) {
                this.montoDeCuota = montoDeCuota + (montoDeCuota * this.feePrestamo / 100);
                this.montoDeCuota = this.montoDeCuota.toFixed(2);
            } else {
                this.montoDeCuota = 0;
            }



        }

    }
}).mount('#app')