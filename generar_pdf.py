#!/usr/bin/env python3
import markdown
from weasyprint import HTML, CSS
import os

# Leer el archivo Markdown
with open("DOCUMENTACION.md", "r", encoding="utf-8") as f:
    md_content = f.read()

# Convertir Markdown a HTML
html_content = markdown.markdown(
    md_content,
    extensions=["extra", "codehilite", "tables", "toc"]
)

# Plantilla HTML con estilos
html_template = f"""
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Documentación del Proyecto Housing</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        
        body {{
            font-family: 'Inter', Arial, sans-serif;
            font-size: 11pt;
            line-height: 1.6;
            color: #333;
            max-width: 800px;
            margin: 0 auto;
            padding: 40px;
        }}
        
        h1 {{
            font-size: 26pt;
            color: #1a73e8;
            border-bottom: 3px solid #1a73e8;
            padding-bottom: 10px;
            margin-top: 0;
        }}
        
        h2 {{
            font-size: 20pt;
            color: #202124;
            margin-top: 30px;
            border-bottom: 1px solid #e0e0e0;
            padding-bottom: 5px;
        }}
        
        h3 {{
            font-size: 16pt;
            color: #5f6368;
            margin-top: 25px;
        }}
        
        h4 {{
            font-size: 13pt;
            color: #5f6368;
            margin-top: 20px;
        }}
        
        code {{
            background-color: #f1f3f4;
            padding: 2px 6px;
            border-radius: 4px;
            font-family: 'Consolas', monospace;
            font-size: 10pt;
        }}
        
        pre {{
            background-color: #2d2d2d;
            color: #f8f8f2;
            padding: 15px;
            border-radius: 8px;
            overflow-x: auto;
            margin: 15px 0;
        }}
        
        pre code {{
            background: none;
            color: inherit;
            padding: 0;
        }}
        
        ul, ol {{
            margin: 10px 0;
            padding-left: 25px;
        }}
        
        li {{
            margin: 5px 0;
        }}
        
        table {{
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0;
        }}
        
        th, td {{
            border: 1px solid #e0e0e0;
            padding: 10px;
            text-align: left;
        }}
        
        th {{
            background-color: #f8f9fa;
            font-weight: 600;
        }}
        
        blockquote {{
            border-left: 4px solid #1a73e8;
            padding-left: 15px;
            margin: 15px 0;
            color: #5f6368;
        }}
        
        .toc {{
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
        }}
        
        .toc h3 {{
            margin-top: 0;
        }}
        
        @page {{
            size: A4;
            margin: 2cm;
        }}
    </style>
</head>
<body>
    {html_content}
</body>
</html>
"""

# Generar el PDF
print("Generando PDF...")
HTML(string=html_template).write_pdf("DOCUMENTACION_COMPLETA.pdf")
print("¡PDF generado exitosamente: DOCUMENTACION_COMPLETA.pdf!")
