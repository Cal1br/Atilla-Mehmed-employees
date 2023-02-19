import axios from "axios";
import React from "react";

function CSVUpload(): JSX.Element {

    const [selectedFile, setSelectedFile] = React.useState();
    const [isFilePicked, setIsFilePicked] = React.useState(false);

    const changeHandler = (event) => {
        setSelectedFile(event.target.files[0]);
        setIsFilePicked(true);
    };
    const handlePhotoUpload = (event: any) => { //todo fix any
        event.preventDefault();

        const url = 'http://localhost:8080/';
        const formData = new FormData();
        formData.append("File", selectedFile);
        axios.post(url, formData).then(
            () => {
                console.log("Succes");
            },
            () => {
                console.error("Error:");
            }
        );
    };

    return (
        <form onSumbit={handlePhotoUpload}>
            <input type="file" name="file" onChange={changeHandler}/>
            <button>Upload CSV</button>
        </form>
    )
}

export default CSVUpload;