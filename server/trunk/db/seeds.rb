# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ :name => 'Chicago' }, { :name => 'Copenhagen' }])
#   Mayor.create(:name => 'Daley', :city => cities.first)
[User, Material, TimeEntryType, TimeEntry].each do |cls|
  cls.delete_all
end
#User.create!(:first_name => "Sec", :last_name => "Retary", :email => 'secretary@mrt.ch', :password => 'mrt', :type => User::TYPES[:secretary])
#User.create!(:first_name => "Field", :last_name => "Worker", :email => 'field_worker@mrt.ch', :password => 'mrt', :type => User::TYPES[:field_worker])
Secretary.create!(:first_name => "Sec", :last_name => "Retary", :email => 'secretary@mrt.ch', :password => 'mrt')
FieldWorker.create!(:first_name => "Field", :last_name => "Worker", :email => 'field_worker@mrt.ch', :password => 'mrt')

Customer.delete_all
"Waldemar Lamprecht, Otto Traugott, Hermann Elmo, Veit Ingolf, Björn Simon, 
Klaus Dietfried, Gotthilf Wieland, June Gretchen, Taryn Shelia, Tarah Dione, 
Angelica Xavia, Demontongue Catbroom, Silverbeam Blackmoon, Whirl Gnaw, Nike Medb, 
Pia Ligeia, Ásdís Eirene, Beatrix Afra, Rochus Rocco, Eirene Felicianus, Hrodger Amantius, 
Sigimund Lóegaire, Friðþjófr Ramessu, Yima Philander, Enlil Prabhu, Owain Iovis, Týr Cronus, 
Partha Pramoda, Gawain Jarl, Cupid Amulius".split(',').each do |name|
  f, l = name.strip.split(' ')
  Customer.create!(:first_name => f.strip, :last_name => l.strip)
end

Material.create!([
    {:catalog_number => "3951.100", :description => "Füllventil Universal zu Spülkasten Geberit", :dimensions => "", :price => 34.0},
    {:catalog_number => "865005-336", :description => "Spültisch-Garnitur GEBERIT 1-teilig", :dimensions => "1 1/2 x 56mm", :price => 30.0},
    {:catalog_number => "046.101.000", :description => "Standrohrventil zu Spültisch Franke, ohne Sieb", :dimensions => "1 1/2", :price => 21.5},
    {:catalog_number => "863019-000", :description => "Bogen 90° GEBERIT mit Verschraubung", :dimensions => "56mm", :price => 15.0},
    {:catalog_number => "086105-000", :description => "Überwurfmutter GEBERIT Schleifing, Dichtung", :dimensions => "56mm", :price => 6.3},
    {:catalog_number => "51.000064", :description => "Verteilerkasten Grösse 3", :dimensions => "800mm", :price => 326.0},
    {:catalog_number => "51.043704", :description => "Anschlussverschraubung MS", :dimensions => "3/4-17x2", :price => 8.95},
    {:catalog_number => "51.023601", :description => "Anschlussverschraubung vern.", :dimensions => "3/4-16x2", :price => 7.05},
    {:catalog_number => "", :description => "Verbrauchs- und Kleinmaterial", :dimensions => "", :price => 3},
    {:catalog_number => "aa", :description => "Einsatz Servicewagen", :dimensions => "", :price => 18},
  ])

materials = Material.all
TimeEntryType.create!(:description => "Heizung ansehen", :time_entry_type_materials => [
    TimeEntryTypeMaterial.new(:material => materials[9]),
  ])
TimeEntryType.create!(:description => "Lavabo wechseln", :time_entry_type_materials => [
    TimeEntryTypeMaterial.new(:material => materials[0]),
    TimeEntryTypeMaterial.new(:material => materials[1]),
    TimeEntryTypeMaterial.new(:material => materials[4], :amount => 5),
    TimeEntryTypeMaterial.new(:material => materials[9]),
  ])
TimeEntryType.create!(:description => "Wasserzähler ersetzen", :time_entry_type_materials => [
    TimeEntryTypeMaterial.new(:material => materials[8]),
    TimeEntryTypeMaterial.new(:material => materials[9]),
  ])


# It may be a good idea to have the same test data as the initial data? The use this command to load all fixtures!
# system("rake db:fixtures:load")



