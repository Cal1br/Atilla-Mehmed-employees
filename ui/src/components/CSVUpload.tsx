import axios from "axios";
import React, {ChangeEvent, FormEvent} from "react";
import PairDurationModel from "../model/PairDurationModel";

interface CSVUploadProps{
    onCompleteCallback?: (rows: PairDurationModel[]) => void;
}
function CSVUpload(props: CSVUploadProps): JSX.Element {

    const [selectedFile, setSelectedFile] = React.useState<any>('');

    const changeHandler = (event:ChangeEvent<HTMLInputElement>) => {
        if (event.target.files){
            setSelectedFile(event.target.files[0]);
        }
    };
    const handlePhotoUpload = (event: FormEvent<HTMLFormElement>) => { //todo fix any
        event.preventDefault();
        const url = 'http://localhost:8080/project/upload';
        const formData = new FormData();
        formData.append("file", selectedFile);
        axios.post(url, formData).then(
            (resp) => {
                if(props.onCompleteCallback){
                    props.onCompleteCallback(resp.data)
                }
            }
        );
    };

    return (
        <form style={{margin:10}} onSubmit={handlePhotoUpload}>
            <input type="file" name="file" onChange={changeHandler}/>
            <button>Upload CSV</button>
        </form>
    )
}

export default CSVUpload;