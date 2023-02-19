import React from 'react';
import logo from './logo.svg';
import './App.css';
import PairDurationModel from "model/PairDurationModel";
import DataGrid from 'react-data-grid';
import 'react-data-grid/lib/styles.css'
import CSVUpload from "./components/CSVUpload";


const columns = [
    {width: 100, key: 'fistId', name: 'fistId'},
    {width: 100, key: 'secondId', name: 'secondId'},
    {width: 100, key: 'projectId', name: 'projectId'},
    {width: 100, key: 'duration', name: 'duration'},
]


function App() {
    const [rows, setRows] = React.useState<PairDurationModel[]>([]);

    return (
        <div>
            <header className="App-header"></header>
            <main>
                <CSVUpload></CSVUpload>
                <div>
                    <DataGrid columns={columns}
                              rows={rows}
                              rowKeyGetter={(row) => row.id as number}
                              rowHeight={35}
                          //    onRowsChange={props.onRowsChange}
                              className="fill-grid"
                              direction={'ltr'}
                    />
                </div>
            </main>
        </div>
    );
}

export default App;
