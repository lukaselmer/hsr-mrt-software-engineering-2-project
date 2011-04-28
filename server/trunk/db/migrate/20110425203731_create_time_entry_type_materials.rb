class CreateTimeEntryTypeMaterials < ActiveRecord::Migration
  def self.up
    create_table :time_entry_type_materials do |t|
      t.references :time_entry_type
      t.references :material
      
      t.timestamps
    end
  end

  def self.down
    drop_table :time_entry_type_materials
  end
end
