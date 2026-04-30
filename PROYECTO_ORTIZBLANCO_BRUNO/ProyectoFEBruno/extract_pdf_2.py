import PyPDF2

file_path = r'c:\Users\ortiz\OneDrive\Documentos\repoRemotoProyecto\PROYECTO_ORTIZBLANCO_BRUNO\docu\DocumentacionProyecto_BrunoOrtizBlanco.pdf'
try:
    with open(file_path, 'rb') as file:
        reader = PyPDF2.PdfReader(file)
        text = ''
        # Extraer de la página 15 a la 30 para obtener más detalles técnicos
        start = min(15, len(reader.pages))
        end = min(30, len(reader.pages))
        for i in range(start, end):
            text += reader.pages[i].extract_text()
        print(text)
except Exception as e:
    print(f'Error: {e}')
