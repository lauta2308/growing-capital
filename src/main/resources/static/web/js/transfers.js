const { createApp } = Vue

createApp({
    data() {
        return {

            data: [],
            cuentasCliente: [],
            destinoTransferencia: "",
            cuentaOrigen: "",
            cuentaOrigenSaldo: 0,
            cuentaDestinoPropia: "",
            cuentaDestinoPropiaSeleccionada: "",
            cuentaDestinoAjena: "",
            montoATransferir: "",
            descripcionTransferencia: "",




        }
    },
    created() {
        this.loadData();


    },
    methods: {
        async loadData() {

            await axios.get("/api/clients/current").then(response => {


                this.data = response.data;

                this.cuentasCliente = this.data.account.filter(account => account.status === "ACTIVE");



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

        cerrarSesion: function() {
            axios.post('/api/logout').then(response => window.location.href = "../index.html")
        },
        cuentaOrigenSeleccionada: function(event) {
            this.cuentaOrigenSaldo = this.cuentasCliente.find(cuenta => cuenta.number === this.cuentaOrigen);
            this.cuentaOrigenSaldo = this.cuentaOrigenSaldo.balance;
            this.cuentaDestinoPropia = this.cuentasCliente.filter(account => account.number !== this.cuentaOrigen);

        },

        realizarTransferencia: function() {
            let transferenciaCorrectaMensaje = document.querySelector(".transferencia-correcta");
            let transferenciaIncorrectaMensaje = document.querySelector(".transferencia-incorrecta");

            let sender = this.cuentaOrigen;
            let receiver;
            let ammount = this.montoATransferir;
            let description = this.descripcionTransferencia;

            if (this.destinoTransferencia === "cuentaPropia") {

                receiver = this.cuentaDestinoPropiaSeleccionada;
            } else {
                receiver = this.cuentaDestinoAjena;
            }

            if (sender === receiver) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'La cuenta de origen y destino no pueden ser iguales'

                })
            } else if (sender.length === 0 || receiver.length === 0 || ammount.length === 0 || description.length === 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Completa todos los campos para continuar'

                })
            } else if (ammount < 100) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'El monto mínimo para transferir es de $100'

                })

            } else {

                Swal.fire({
                    title: '¿Confirmar transferencia?',
                    text: "Este cambio no podrá ser revertido",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Si, confirmar'
                }).then(result => {
                    if (result.isConfirmed) {


                        axios.post('/api/transactions', `sender=${sender}&receiver=${receiver}&ammount=${ammount}&description=${description}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                            .then(Swal.fire(
                                'Excelente!',
                                'Transferencia realizada con éxito',
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
                })
            }




        }
    },

    computed: {


    }
}).mount('#app')