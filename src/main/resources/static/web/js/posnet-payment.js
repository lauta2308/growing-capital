const { createApp } = Vue

createApp({
    data() {
        return {

            numeroTarjeta: "",
            cvvTarjeta: "",
            monto: "",
            descripcion: "",




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

        solicitudPagoPosnet: function() {
            let realizarPagoError = document.querySelector(".realizar-pago-error");


            if (this.numeroTarjeta === "" || this.cvvTarjeta === "" || this.amount === "" || this.descripcion === "") {
                realizarPagoError.innerText = "Completa todos los campos para continuar"
                realizarPagoError.style.display = "block";

            } else if (typeof this.numeroTarjeta !== "string") {

                realizarPagoError.innerText = "El número de tarjeta ingresado no es correcto"
                realizarPagoError.style.display = "block";

            } else if (this.numeroTarjeta.length < 19) {
                realizarPagoError.innerText = "La cantidad de números de tarjeta no es correcta. Asegúrate de separar por guiones Ej: 1234-1234-1234-1234"
                realizarPagoError.style.display = "block";

            } else if (this.cvvTarjeta.length > 4) {

                realizarPagoError.innerText = "Cantidad de numeros de cvv incorrecta. Deben ser 3 números"
                realizarPagoError.style.display = "block";

            } else if (this.monto <= 0) {
                realizarPagoError.innerText = "El monto debe ser superior a 0"
                realizarPagoError.style.display = "block";



            } else if (typeof this.monto !== "number") {
                realizarPagoError.innerText = "El monto debe ser sólo números"
                realizarPagoError.style.display = "block";

            } else {
                this.realizarPago();
            }


        },
        realizarPago: function() {
            Swal.fire({
                title: '¿Confirmar pago?',
                text: "Este cambio no podrá ser revertido",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si, confirmar'
            }).then(result => {
                if (result.isConfirmed) {



                    axios.post("/api/posnetpayment", {
                            "cardNumber": this.numeroTarjeta,
                            "cardCvv": this.cvvTarjeta,
                            "amount": this.monto,
                            "description": this.descripcion
                        }).then(Swal.fire(
                            'Excelente!',
                            'Pago realizado con éxito',
                            'success'
                        ))
                        .then(
                            setTimeout(function() {
                                window.location.href = "./posnet-payment.html"
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

            })

        }


    },

    computed: {


    }
}).mount('#app')