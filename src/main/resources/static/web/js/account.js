const { createApp } = Vue

createApp({
    data() {
        return {
            idCuenta: "",
            urlParams: "",
            myParam: "",
            transacciones: "",
            nombreCuenta: "",
            fondoFila: "",
            firstDate: "",
            lastDate: "",
            transaccionesFiltradas: []

        }
    },
    created() {

        this.loadData();
        this.nombreCuentaTransaccion = JSON.parse(localStorage.getItem("numeroCuentaTransaccion"));


    },
    methods: {
        async loadData() {
            this.urlParams = new URLSearchParams(window.location.search),
                this.myParam = this.urlParams.get('id'),
                this.idCuenta = this.myParam,
                await axios.get(`/api/clients/current/accounts/${this.idCuenta}`)
                .then(response =>

                    this.transacciones = response.data.transactions)
            this.transacciones = this.transacciones.sort(function(a, b) { return a.id - b.id });


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

        cambiarFondoFila: function(transactionType) {
            if (transactionType === "DEBITO") {
                this.fondoFila = bgRed;
            }

        },
        tipoFila: function(tipoTrasaccion) {


            if (tipoTrasaccion === "DEBITO") {
                this.fondoFila = "bgRed";
            } else {
                this.fondoFila = "bgGreen"
            };


        },
        transactionDateFormat: function(date) {

            let fechaTest = new Date(date);
            anioFecha = fechaTest.getFullYear();
            mesFecha = fechaTest.getMonth() + 1;
            diaFecha = fechaTest.getDate();
            horaFecha = fechaTest.getHours();
            minutosFecha = fechaTest.getMinutes();
            return anioFecha + " - " + mesFecha + " - " + diaFecha + " | " + horaFecha + " : " + minutosFecha;

        },
        checkDates: function() {

            let fechaUno = new Date(this.firstDate);
            let fechaDos = new Date(this.lastDate);







            this.transaccionesFiltradas = [];
            for (let i = 0; i < this.transacciones.length; i++) {

                let fechaTransaccion = new Date(this.transacciones[i].date);
                if (fechaTransaccion > fechaUno && fechaTransaccion < fechaDos) {

                    this.transaccionesFiltradas.push(this.transacciones[i]);
                }

            }

        },
        descargarReporte: function() {

            var doc = new jsPDF('p', 'pt', 'letter');
            var margin = 10;
            var scale = (doc.internal.pageSize.width - margin * 2) / document.body.scrollWidth;
            doc.html(document.querySelector(".pdfTransacciones"), {
                x: margin,
                y: margin,
                html2canvas: {
                    scale: scale,
                },
                callback: function(doc) {
                    doc.output("dataurlnewwindow", { filename: "reporte de transacciones.pdf" });
                }
            })

        }



    },

    computed: {




    }
}).mount('#app')