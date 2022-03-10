<div id="top"></div>

# RuVa - RUNTIME VARIABILITY

 

## INSTALACIÓN DE RuVa:


### 1. PRECONDICIONES:


- Eclipse 2019-03 o superior
- FaMaSDK-1.1.1.jar  (included with RuVa)
- commons-io-2.8.0.jar  (included with RuVa)

### 2. INSTALACIÓN

Para instalar el proyecto de RuVa, bien descomprimir la carpeta en la carpeta Workspace de Eclipse o crear un nuevo proyecto e incorporar todos los .java de la carpeta /src y en las referencias del proyecto añadir FaMaSDK y commons-io que están en \lib. Es importante que las librerías que están en \lib se mantengan ahí y no se muevan. 

La carpeta de instalación por defecto es D:\workspace\201903\features\ . Si se cambia, habrá que cambiar también las rutas de los ficheros .init invocados.



### 3. USO

Para ejecutar el proyecto, hay que editar ruva.cfg y hacer que apunte al fichero .init que se va a lanzar, que contiene los comandos para cargar un modelo, insertar 10 características y medir el tiempo. Hay dejado un fichero .init en la carpeta /fm100 llamado secuencia.init , de manera que la última línea de ruva.cfg deberá ser el siguiente. 

fmscript=D:\workspace\201903\features\fm100\secuencia.init

Una vez que RuVa.cfg apunta al fichero .init a lanzar, ya se puede ejecutar RuVa.



 
<!-- CONTACT -->
## Contacto

Alejandro Valdezate - [@valdezate](https://twitter.com/valdezate) - 

Project Link: [https://github.com/valdezate/RuVa](https://github.com/valdezate/RuVa)

<p align="right">(<a href="#top">back to top</a>)</p>

 
