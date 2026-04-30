import PyPDF2

file_path = r'c:\Users\ortiz\OneDrive\Documentos\repoRemotoProyecto\PROYECTO_ORTIZBLANCO_BRUNO\docu\DocumentacionProyecto_BrunoOrtizBlanco.pdf'
try:
    with open(file_path, 'rb') as file:
        reader = PyPDF2.PdfReader(file)
        text = ''
        # Extraer las primeras 15 páginas para obtener un buen resumen sin exceder límites
        for i in range(min(15, len(reader.pages))):
            text += reader.pages[i].extract_text()
        print(text)
except Exception as e:
    print(f'Error: {e}')
