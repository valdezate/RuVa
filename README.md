<div id="top"></div>
 
 
 

INSTALACIÓN DE RuVa:
================================

1. PRECONDICIONES:


- Eclipse 2019-03 o superior
- FaMaSDK-1.1.1.jar  (included with RuVa)
- commons-io-2.8.0.jar  (included with RuVa)

2. INSTALACIÓN

Para instalar el proyecto de RuVa, bien descomprimir la carpeta en la carpeta Workspace de Eclipse o crear un nuevo proyecto e incorporar todos los .java de la carpeta /src y en las referencias del proyecto añadir FaMaSDK y commons-io que están en \lib. Es importante que las librerías que están en \lib se mantengan ahí y no se muevan. 

La carpeta de instalación por defecto es D:\workspace\201903\features\ . Si se cambia, habrá que cambiar también las rutas de los ficheros .init invocados.



3. USO

Para ejecutar el proyecto, hay que editar ruva.cfg y hacer que apunte al fichero .init que se va a lanzar, que contiene los comandos para cargar un modelo, insertar 10 características y medir el tiempo. Hay dejado un fichero .init en la carpeta /fm100 llamado secuencia.init , de manera que la última línea de ruva.cfg deberá ser el siguiente. 

fmscript=D:\workspace\201903\features\fm100\secuencia.init

Una vez que RuVa.cfg apunta al fichero .init a lanzar, ya se puede ejecutar RuVa.




### Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* npm
  ```sh
  npm install npm@latest -g
  ```

### Installation

1. Get a free API Key at [https://example.com](https://example.com)
2. Clone the repo
   ```sh
   git clone https://github.com/github_username/repo_name.git
   ```
3. Install NPM packages
   ```sh
   npm install
   ```
4. Enter your API in `config.js`
   ```js
   const API_KEY = 'ENTER YOUR API';
   ```

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [ ] Feature 1
- [ ] Feature 2
- [ ] Feature 3
    - [ ] Nested Feature

See the [open issues](https://github.com/github_username/repo_name/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#top">back to top</a>)</p>


 

<!-- CONTACT -->
## Contact

Alejandro VAldezate - [@valdezate](https://twitter.com/valdezate) - vzthesis

Project Link: [https://github.com/valdezate/RuVa](https://github.com/valdezate/RuVa)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* []()
* []()
* []()

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo_name/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo_name/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo_name/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo_name.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo_name/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[product-screenshot]: images/screenshot.png
